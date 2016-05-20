
# boot-rev

Boot task for file revisions (Naming files by version, hash etc.)

![Clojars Project](http://clojars.org/mrmcc3/boot-rev/latest-version.svg)

### Usage

Use in `build.boot`:

```clj
(set-env! :dependencies '[[mrmcc3/boot-rev "0.1.0-SNAPSHOT"]])
(require '[mrmcc3.boot-rev :refer [rev]])

;; fingerprint all js files
(deftask demo-rev []
  (comp
    (rev :files [#"^.*\.js$"])
    (target)))
```

try a demo in this repo:

```bash
boot demo-rev
```

By [adding metadata to `fileset`][meta] you can lookup paths in a following task:

[meta]: https://github.com/boot-clj/boot/wiki/Task-Writer%27s-Guide#metadata

```clj
;; you can get the reved path from the original path
(rev-path fileset "out/main.js") ;; -> out/main.ae34fc11.js
```

### LICENSE

Copyright © 2015 Michael McClintock

Distributed under the Eclipse Public License, the same as Clojure.
