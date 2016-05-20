(def +project+ 'mrmcc3/boot-rev)
(def +version+ "0.1.0")

(set-env!
  :project +project+
  :version +version+
  :asset-paths #{"demo-assets"}
  :source-paths #{"src"}
  :dependencies '[[org.clojure/clojure "1.8.0" :scope "provided"]
                  [boot/core "2.5.5" :scope "provided"]
                  [adzerk/bootlaces "0.1.13" :scope "test"]])

(require '[adzerk.bootlaces :refer :all]
         '[mrmcc3.boot-rev :refer [rev]])

(bootlaces! +version+)

(task-options!
  pom {:project     +project+
       :version     +version+
       :description "Boot task for revisioning files (by version, hash etc.)"
       :url         "https://github.com/mrmcc3/boot-rev"
       :scm         {:url "https://github.com/mrmcc3/boot-rev"}
       :license     {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask demo-rev []
  (comp
    (rev :files [#"^.*\.js$"])
    (target)))
