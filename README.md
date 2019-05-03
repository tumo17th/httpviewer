# HttpViewer / Http閲覧ツール

---
### Outline
Simple Application for viewing Http Header &amp; Body  

### How to use
1. Download httpviewer-bin.zip from [Release 1.0.1](https://github.com/tumo17th/httpviewer/releases)  
  (You can get it from src by runnning maven:package >> httpviewer-bin.zip is created under target folder.)  
2. Unzip httpviewer-bin.zip.
3. Edit config/httpviewer.properties >> modify targetURL's value.(ex:http://google.com).
4. Kick run.bat >> Console appears and it shows result.
5. Result file named [response_yyyyMMdd_HHmmss.txt] is created.

---

### 概要
Httpの内容(HeaderとBody)を確認するシンプルなツールです。

### 使い方
1. httpviewer-bin.zipを[Release 1.0.1](https://github.com/tumo17th/httpviewer/releases)よりダウンロードする。  
  (srcからmaven:packageを実行して取得することも可能 >> targetフォルダにhttpviewer-bin.zipが作成される)  
2. httpviewer-bin.zipを解凍。
3. config/httpviewer.propertiesの値を編集 >> targetURLの値を変更する。(例:http://google.com)
4. run.batを実行 >> コンソールが立ち上がり、結果が表示される。
5. resultフォルダに、「response_yyyyMMdd_HHmmss.txt」という結果ファイルが作成される。

---
