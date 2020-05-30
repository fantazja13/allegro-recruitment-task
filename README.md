# Read below instructions before running tests

### Authorization
You need to register new account [here](https://apps.developer.allegro.pl/new) before running those tests.
Enter generated **client id** and **client credentials** in *data.properties* file:
```
authorization.app.credentials.client_id=<client_id>
authorization.app.credentials.client_secret=<client_secret>
```

You can run tests manually in your IDE or using Maven.

Tests were implemented based on Allegro API documentation ([here](https://developer.allegro.pl/documentation/#tag/Categories-and-parameters))
for Categories and Parameters endpoints. No additional requirements were provided, 
hence no validation for exact value of most fields.

Technology stack:
* Java 8
* Rest Assured 
* TestNG