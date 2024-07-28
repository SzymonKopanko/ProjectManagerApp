package skopanko.projectmanagerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "skopanko.projectmanagerapi.entity")
public class ProjectManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerApiApplication.class, args);
    }

}
