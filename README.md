# AWS LAMBDA を使ってファイルアップロード

## ビルドとAWSへのデプロイ

```
$ activator assembly
```

## AWSにデプロイ

### TODO

* target/scala-2.11/multipart-test-assembly-1.0.jar をデプロイ
* jp.pigumer.Handler::handleRequest

## API Gatewayの設定

### TODO

#### 統合リクエストに multipart/form-data のマッピングを追加

```
#set($inputParams = $util.base64Encode($input.body))
"$inputParams"
```

## API Gateway の URL を設定

environment.js に URL を記述

```
var AWS_LAMBDA_URL='URL'
```
