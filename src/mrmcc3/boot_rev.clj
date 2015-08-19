(ns mrmcc3.boot-rev
  (:require [boot.core :refer :all]
            [boot.util :refer :all]
            [clojure.java.io :as io]))

(defn value-versioner [v f]
  (let [path (tmp-path f)
        i (.lastIndexOf path ".")
        before (str (subs path 0 i) ".")
        after (subs path i)]
    (str before v after)))

(defn fingerprinter [f]
  (value-versioner (subs (:id f) 0 8) f))

(deftask rev
  "revision files. for example example main.js -> main.ae3f41fe.js. by default will use the first 8
  characters of the md5 hash as calculated by boot. Alternatively a constant version value can be
  supplied. For custom versioning a symbol can be provided which will be resolved and invoked
  with the tmpfile to produce a version."
  [f files #{regex} "only files that match one of the supplied regexs will be versioned"
   v version VER str "supply a constant version value to all files"
   s sym SYM sym "a symbol to be resolved and invoked along with the tmpfile to produce the version"
   c copy bool "if true will create versioned copies otherwise it will replace the initial files"]
  (let [tmp (tmp-dir!)
        versioner (cond
                    version (partial value-versioner version)
                    sym (resolve sym)
                    :else fingerprinter)]
    (with-pre-wrap fileset
      (info "\nVersioning files ...\n")
      (let [files-to-rev (->> fileset output-files (by-re files))
            reducer! (fn [m f]
                       (let [in-path (tmp-path f)
                             out-path (versioner f)]
                         (info "• %s ⇒  %s\n" in-path out-path)
                         (spit (io/file tmp out-path) (slurp (tmp-file f)))
                         (assoc m out-path {:original-path in-path})))
            meta-map (reduce reducer! {} files-to-rev)]
        (-> fileset
            (add-resource tmp)
            (add-meta meta-map)
            (cond-> (not copy) (rm files-to-rev))
            commit!)))))

(defn rev-path [fileset original-path & [default]]
  (get (->> fileset output-files
            (filter :original-path)
            (map (fn [f] [(:original-path f) (tmp-path f)]))
            (into {}))
       original-path
       (if default default original-path)))
