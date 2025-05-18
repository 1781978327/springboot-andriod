package com.example.ai_1.util;

import android.content.Context;
import android.widget.TextView;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.ext.tasklist.TaskListPlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.syntax.Prism4jThemeDarkula;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;

public class MarkdownRenderer {
    private static Markwon markwon;

    public static void renderMarkdown(Context context, TextView textView, String markdown) {
        if (markwon == null) {
            final Prism4j prism4j = new Prism4j(new CustomGrammarLocator());
            markwon = Markwon.builder(context)
                    .usePlugin(StrikethroughPlugin.create())
                    .usePlugin(TablePlugin.create(context))
                    .usePlugin(TaskListPlugin.create(context))
                    .usePlugin(HtmlPlugin.create())
                    .usePlugin(SyntaxHighlightPlugin.create(prism4j, Prism4jThemeDarkula.create()))
                    .build();
        }
        markwon.setMarkdown(textView, markdown);
    }
} 