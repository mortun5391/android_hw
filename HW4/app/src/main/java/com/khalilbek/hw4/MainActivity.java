package com.khalilbek.hw4;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorText;
    private final WikiAdapter adapter = new WikiAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorText = findViewById(R.id.errorText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            fetchPosts();
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            showError("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fetchPosts() {
        progressBar.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://neerc.ifmo.ru/wiki/")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getPageHtml().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<WikiItem> items = parseWikiHtml(response.body());
                    if (!items.isEmpty()) {
                        adapter.setItems(items);
                    } else {
                        showError("Структура страницы не распарсена");
                    }
                } else {
                    showError("Сервер вернул ошибку: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showError("Запрос не удался: " + t.getMessage());
            }
        });
    }

    private List<WikiItem> parseWikiHtml(String html) {
        List<WikiItem> items = new ArrayList<>();
        String pageLink = "https://neerc.ifmo.ru/wiki/index.php?title=%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D1%8B_%D0%B8_%D1%81%D1%82%D1%80%D1%83%D0%BA%D1%82%D1%83%D1%80%D1%8B_%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85";

        Document doc = Jsoup.parse(html);
        Element content = doc.selectFirst("#mw-content-text");
        if (content == null) {
            return items;
        }

        // Собираем карту заголовок->якорь, чтобы список ссылался на реальный раздел, если возможно
        Map<String, String> headingMap = new HashMap<>();
        for (Element h : content.select("h2,h3,h4,h5,h6")) {
            Element span = h.selectFirst("span.mw-headline");
            if (span != null && !span.text().isEmpty() && !span.id().isEmpty()) {
                headingMap.put(cleanText(span.text()), pageLink + "#" + span.id());
            }
        }

        Elements headings = content.select("h2, h3");
        for (Element heading : headings) {
            String headingText = cleanText(heading.text());
            if (headingText.isEmpty()) continue;

            Element span = heading.selectFirst("span.mw-headline");
            String idAttr = (span != null) ? span.id() : "";
            String sectionLink = pageLink + (idAttr.isEmpty() ? "" : "#" + idAttr);

            StringBuilder bodyBuilder = new StringBuilder();
            for (Element sibling = heading.nextElementSibling(); sibling != null;
                 sibling = sibling.nextElementSibling()) {

                String tag = sibling.tagName();
                if (tag.matches("h1|h2|h3|h4|h5|h6")) break;

                if ("p".equals(tag)) {
                    String part = cleanText(sibling.text());
                    if (!part.isEmpty()) {
                        bodyBuilder.append(part).append("\n\n");
                    }
                } else if ("ul".equals(tag) || "ol".equals(tag)) {
                    for (Element li : sibling.select("li")) {
                        String part = cleanText(li.text());
                        if (part.isEmpty()) continue;

                        String itemLink = null;
                        Element liLink = li.selectFirst("a[href]");
                        if (liLink != null) {
                            String href = liLink.attr("href");
                            if (!href.isEmpty()) {
                                if (href.startsWith("http")) {
                                    itemLink = href;
                                } else if (href.startsWith("/")) {
                                    itemLink = "https://neerc.ifmo.ru" + href;
                                } else {
                                    itemLink = "https://neerc.ifmo.ru/wiki/" + href;
                                }
                            }
                        }

                        if (itemLink == null || itemLink.isEmpty()) {
                            itemLink = headingMap.getOrDefault(part, sectionLink);
                        }

                        bodyBuilder.append("• <a href=\"").append(itemLink).append("\">")
                                .append(part).append("</a>\n");
                    }
                    bodyBuilder.append("\n");
                }
            }

            String body = bodyBuilder.toString().trim();
            if (!body.isEmpty()) {
                body += "\n\nПолный конспект: <a href=\"" + sectionLink + "\">Открыть</a>";
                items.add(new WikiItem(headingText, body));
            }
        }

        if (items.isEmpty()) {
            Element p = content.selectFirst("p");
            if (p != null) {
                String text = cleanText(p.text());
                if (!text.isEmpty()) {
                    items.add(new WikiItem("Описание", text + "\n\nПолный конспект: <a href=\"" + pageLink + "\">Открыть</a>"));
                }
            }
        }

        return items;
    }

    private String cleanText(String s) {
        if (s == null) return "";
        String cleaned = s.replaceAll("\\[math\\].*?\\[/math\\]", "");
        cleaned = cleaned.replaceAll("\\r\\n", "\n");
        cleaned = cleaned.replaceAll("\n{2,}", "\n\n");
        cleaned = cleaned.replaceAll("[ \t]{2,}", " ");
        cleaned = cleaned.replaceAll("[ \t]*\n[ \t]*", "\n");
        return cleaned.trim();
    }

    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisibility(View.VISIBLE);
    }
}
