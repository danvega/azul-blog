package dev.danvega.azulblog;

import dev.danvega.azulblog.post.JsonPlaceholderService;
import dev.danvega.azulblog.post.Post;
import dev.danvega.azulblog.post.PostRepository;
import dev.danvega.azulblog.post.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	JsonPlaceholderService jsonPlaceholderService() {
		RestClient client = RestClient.create("https://jsonplaceholder.typicode.com");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory
				.builderFor(RestClientAdapter.create(client))
				.build();
		return factory.createClient(JsonPlaceholderService.class);
	}

	@Bean
	CommandLineRunner commandLineRunner(PostRepository postRepository, PostService postService, JsonPlaceholderService jsonPlaceholderService) {
		return args -> {
//			List<Post> posts = postService.loadPosts();
			List<Post> posts = jsonPlaceholderService.getPosts();
			posts.forEach(postRepository::create);
		};
	}

}
