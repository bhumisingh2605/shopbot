# ShopBot

Sample e-commerce support agent (Spring Boot, Spring AI, Ollama) used as the agent under test for AgentProbe.

## Prerequisites

- Java 21
- Maven (or the included Maven Wrapper)
- Ollama installed and running

## Run

Pull the model:

```bash
ollama pull llama3.1
```

Start the application from the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

The sample commands below assume the application runs at `http://localhost:8082`. Make sure `server.port` in `src/main/resources/application.yml` is set to `8082`.

## Try it

Check the status of order `1043`:

```bash
curl -X POST http://localhost:8082/chat -H "Content-Type: application/json" -d "{\"message\":\"Status of order 1043?\",\"sessionId\":\"t1\"}"
```

## Prompt Versions

To run the deliberately weaker prompt, set this in `src/main/resources/application.yml`:

```yaml
shopbot:
  version: v2-weak
```

Use the default configured version for the normal prompt.

## AgentProbe Integration

ShopBot acts as the sample agent that AgentProbe can test. It provides a chat endpoint that can be used to evaluate agent responses and tool calls.

