package practice.communityservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import practice.communityservice.dto.ArticleDto;
import practice.communityservice.repository.ArticleRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void UpdateViewCountConcurrencyTest(){

        Long postId = articleRepository.postArticle(1L, 1L, "ConcurrencyTest", "Test");

        // given
        class ViewThread extends Thread {
            private int num;
            public ViewThread(int num){
                this.num = num;
            }
            @Override
            public void run() {
                try {
                    articleRepository.updateViewCount(postId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Thread[] threads = new ViewThread[100];
        for(int i = 0; i < 100; i++){
            threads[i] = new ViewThread(i);
        }
        // when
        for(int i = 0; i < 100; i++){
            threads[i].start();
        }
        // then
        Optional<ArticleDto> article = articleRepository.getArticleById(postId);
        assertThat(article.isPresent()).isTrue();
        assertThat(article.get().getViewCount()).isEqualTo(100);
    }

}