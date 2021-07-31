package uz.mehrojbek.appmagazinresiduebot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import uz.mehrojbek.appmagazinresiduebot.entity.Product;
import uz.mehrojbek.appmagazinresiduebot.entity.enums.BranchTypeEnum;
import uz.mehrojbek.appmagazinresiduebot.repository.ProductRepository;


import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BotInlineModeService {
    private final ProductRepository productRepository;

    public AnswerInlineQuery answerInlineQuery(InlineQuery inlineQuery){

        String query = inlineQuery.getQuery();
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());

        List<Product> products = productRepository.findAllByNameContainingOrBrandContainingOrTariffContaining(query, query, query);

        int maxCount = 50;
        if (products.size()<=50){
            maxCount = products.size();
        }
        List<InlineQueryResult> articles = new ArrayList<>();
        for (int i = 0; i < maxCount; i++) {
            String result = "";
                if (products.get(i).getBranchType().equals(BranchTypeEnum.MAGAZINE)) {
                    result = result + "D. ";
                } else {
                    result = result + "S. ";
                }
                String size = "";
                if (products.get(i).getSize() == null){
                    size = "yo'q";
                }else {
                    size = String.valueOf(products.get(i).getSize());
                }
                result = result  + products.get(i).getBrand() + " " + products.get(i).getName() +
                        " " + products.get(i).getTariff() + " " + size;

            InlineQueryResultArticle article = new InlineQueryResultArticle();
            article.setTitle(result);
            article.setId("mahsulot id:" + products.get(i).getId());
            article.setInputMessageContent(new InputTextMessageContent("mahsulot id:" + products.get(i).getId()));
            articles.add(article);
        }
        answerInlineQuery.setResults(articles);
        return answerInlineQuery;
    }
}
