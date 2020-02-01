[![Build Status](https://dev.azure.com/validaria/openvalidation/_apis/build/status/openVALIDATION/openVALIDATION%20master?branchName=master)](https://dev.azure.com/validaria/openvalidation/_build/latest?definitionId=5&branchName=master)
![Azure DevOps tests (compact)](https://img.shields.io/azure-devops/tests/validaria/openvalidation/5?compact_message)
[![Maven Central](https://img.shields.io/maven-central/v/io.openvalidation/openvalidation-rest-api)](https://search.maven.org/search?q=a:openvalidation-rest-api)
[![Follow us on Twitter](https://img.shields.io/twitter/follow/openVALIDATION?style=social)](https://twitter.com/openVALIDATION)


OpenVALIDATION enables you to generate validation rules from natural language-like expressions in English and German without any knowledge in programming.
The validation rules can be automatically translated by openVALIDATION into Java, JavaScript, C# or Python, with more languages to come!

# openvalidation-rest

rest api wrapper for openvalidation

## Build and Run

Build with maven:

``mvn clean package``

Run via:

``java -jar target/ov-rest.jar --server.port=8080``

(specify server port wanted for hosting service, 8080 might be already assigned in development setting)


## Test

``mvn clean test``
