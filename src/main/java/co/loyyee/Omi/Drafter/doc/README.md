# Spring AI Autoconfiguration.

When using Spring AI\
one of the ways to setup the configuration is to include information in the application.properties/.yml file.

example in .`yml
```yml 
spring:
  ai: # auto configuration
    openai:
      api-key: ${OPENAI_KEY}
      chat:
        options:
          model: gpt-3.5-turbo
```
example in .properties
```properties
spring.ai.openai.api-key=${OPENAI_KEY}
spring.ai.openai.chat.options.model="gpt-3.5-turbo"
spring.ai.openai.chat.options.temperature="0.4"
```

#### Note:
temperature - how creative the model will be.