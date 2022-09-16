# LinkShortener

HTTP REST service, that generate short links. Build on Spring Boot 2, Java 8, H2 embedded and Lombok.
App made for this [task (RUS)](https://drive.google.com/file/d/1RLGRpvO4QhE-Ocm9ZjHH7Vs3h0ss6iZ7/view)

## Running app locally

### With Maven command line

```cmd
git clone https://github.com/ilya-vdovenko/LinkShortener.git
./mvnw spring-boot:run
```

### Use .jar file

```cmd
java -jar linkshortener-<num_of_version>.jar
```

## Usage of service

### POST

Send link to get short link. Example:

``` 
{
  “original”: “https://some-server.com/some/url?some_param=1”
}
```

Response: 

```
{
  “link”: “/l/some-short-name”
}
```

### GET

Redirect to original url:
```/l/some-short-name```

Get stats for url: ```/stats/some-short-name```

Example of response:

```
{
“link”: “/l/some-short-name”,
“original”: “http://some-server.com/some/url”
“rank”: 1,
“count”: 100500
}
```

Get raiting of urls: ```/stats?page=1&count=2``` (default page = 0, count = 10 (<100))

Example of response:
```
[
  {
    “link”: “/l/some-short-name”,
    “original”: “http://some-server.com/some/url”
    “rank”: 1,
    “count”: 100500
  },
  {
    “link”: “/l/some-another-short-name”,
    “original”: “http://another-server.com/some/url”
    “rank”: 2,
    “count”: 40000
  }
]
```

You can use swagger ui http://localhost:8080/swagger-ui.html for quick requests.

## License

LinkShortener is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).