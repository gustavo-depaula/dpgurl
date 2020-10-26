<p align="center">
  <img width="50%" src=".github/logo.png" alt="dpgurl logo">
</p>

# dpgurl

_**d**e**p**aula**g**u's expirable **url** shortner service, written, with love, in clojure! ❤️_

## 🚀 Installation & Usage

This project uses [Leiningen](https://leiningen.org/) to manage dependencies and automation. 
You can use [brew](https://formulae.brew.sh/formula/leiningen) or another lesser package manager
to install it.
This project uses this version of Leiningen (`lein --version`):
```bash
Leiningen 2.9.1 on Java 1.8.0_192 OpenJDK 64-Bit Server VM
```

Having `leiningen` installed, run the following command to install the dependencies.

```bash
lein deps
```

You can run the application either by storing data use an in-memory "database" (implemented as a
clojure atom), or a use Redis instance by passing a `REDIS_URL` environment variable.
This will start a server on `localhost:3000`:
```bash
lein ring server-headless
REDIS_URL="redis://localhost:6379" lein ring server-headless
```

## 📓 API documentation
Production url: `https://dpgurl.herokuapp.com`

### POST /url
Create a new short url from a long url.

Request body:
```json
{
  "link": "https://google.com"
}
```

Response body:
```json
{
  "short-url": "bPt_lg",
  "long-url": "https://google.com"
}
```

### GET /:{short_url}
Example: `localhost:3000/bPt_lg`:

If the short url exists, a response with status `302 moved temporarily` and
the corresponding long url as `headers.location` is returned. If the short url
does not exists, a `404 not found` status is returned. 

## 📘 Brief architecture documentation
The application has 4 basic modules
- `db.clj`: Takes care of data storage. Switches between using redis and an in-memory clojure map in an atom. We set a `ttl` of 24 hours in Redis, this gives us the short url expirability. It has two functions as its public interface:
  - `(get-long-url [short])`
  - `(insert-url-into-db [short long])`
- `url.clj`: Takes care of short url generation. We use a random number provided by [`clj-crypto.core.create-salt`](https://github.com/macourtney/clj-crypto) and encode it with `base64`. All short urls have 6 chars, so we have 64^6 (~69 billion) possible urls. There's a chance of collision. If a short url is already present on the database, we create a new one (take a look at `core.clj`). The module has one function as its public interface:
  - `(make-short-url [])`
- `core.clj` (find a better name, perhaps?): Takes the building blocks from `db.clj` & `url.clj`, glue everything together and defines two workflows, one to create a short url, and another to get the long url.
- `handler.clj`: configures app routes and call the workflows defined in `core.clj`.

## ❤️ Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## 📝 TODO List
- [ ] Write automated tests 😭
- [ ] Add a UI, with clojurescript 📱
- [ ] Measure scalability ⚖️
- [ ] Add a Dockerfile 📦
- [ ] Create a CLI `dpgurl google.com` ⌨️

## 📜 License
[MIT](https://choosealicense.com/licenses/mit/)

```MIT License
Copyright (c) 2020 Gustavo de Paula

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
