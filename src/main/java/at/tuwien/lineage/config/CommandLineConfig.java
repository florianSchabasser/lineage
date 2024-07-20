package at.tuwien.lineage.config;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class CommandLineConfig {

    @Bean
    public PromptProvider promptProvider() {
        return () -> new AttributedString("my-custom-prompt >> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }

}
