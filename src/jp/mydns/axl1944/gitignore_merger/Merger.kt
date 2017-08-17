package jp.mydns.axl1944.gitignore_merger

import java.io.File

fun main(args: Array<String>) {
    val merger = Merger(args)
    merger.show()
}

class Merger(private val args: Array<String>) {
    fun show() {
        if (args.isEmpty() || args.size < 2) {
            printUsage()
            return
        }

        val dir = File(args[0])
        if (!dir.isDirectory) {
            printUsage()
            return
        }

        val argsLanguages = ArrayList<String>()
        (1..(args.size - 1)).mapTo(argsLanguages) { "${args[it]}.gitignore" }

        val result = HashSet<String>()
        val langFiles: Array<File> = dir.listFiles()
        for (langFile in langFiles) {
            val langFileName = langFile.name
            if (langFile.isDirectory && langFileName == "Global") {
                // if find 'Global' directory
                langFile.listFiles().filter {
                    it.name.endsWith(".gitignore") && argsLanguages.contains(it.name)
                }.forEach {
                    it.readLines().filter { !it.startsWith("#") }.forEach { result.add(it) }
                }
                continue
            }

            if (langFileName.startsWith(".") || langFileName.endsWith(".md")) {
                continue
            }

            if (argsLanguages.contains(langFileName)) {
                langFile.readLines().filter { !it.startsWith("#") }.forEach { result.add(it) }
            }
        }

        result.forEach(::println)
    }

    private fun printUsage() {
        val usage = """
            Usage: java -jar gitignore_merger.jar /PATH/TO/gitignore LANGUAGE1 LANGUAGE2...

            Specifiable programming languages are: https://github.com/github/gitignore
            Or see below languages.

            Languages:
            Actionscript, Ada, Agda, Android, AppEngine, AppceleratorTitanium, ArchLinuxPackages, Autotools, C++, C, CFWheels, CMake, CUDA, CakePHP, ChefCookbook, Clojure, CodeIgniter, CommonLisp, Composer, Concrete5, Coq, CraftCMS, D, DM, Dart, Delphi, Drupal, EPiServer, Eagle, Elisp, Elixir, Elm, Erlang, ExpressionEngine, ExtJs, Fancy, Finale, ForceDotCom, Fortran, FuelPHP, GWT, Gcov, GitBook, Go, Gradle, Grails, Haskell, IGORPro, Idris, Java, Jboss, Jekyll, Joomla, Julia, KiCad, Kohana, Kotlin, LabVIEW, Laravel, Leiningen, LemonStand, Lilypond, Lithium, Lua, Magento, Maven, Mercury, MetaProgrammingSystem, Nanoc, Nim, Node, OCaml, Objective-C, Opa, OpenCart, OracleForms, Packer, Perl, Phalcon, PlayFramework, Plone, Prestashop, Processing, PureScript, Python, Qooxdoo, Qt, R, ROS, Rails, RhodesRhomobile, Ruby, Rust, SCons, Sass, Scala, Scheme, Scrivener, Sdcc, SeamGen, SketchUp, Smalltalk, Stella, SugarCRM, Swift, Symfony, SymphonyCMS, TeX, Terraform, Textpattern, TurboGears2, Typo3, Umbraco, Unity, UnrealEngine, VVVV, VisualStudio, Waf, WordPress, Xojo, Yeoman, Yii, ZendFramework, Zephir

            Misc:
            Anjuta, Ansible, Archives, Bazaar, BricxCC, CVS, Calabash, Cloud9, CodeKit, DartEditor, Dreamweaver, Dropbox, Eclipse, EiffelStudio, Emacs, Ensime, Espresso, FlexBuilder, GPG, JDeveloper, JEnv, JetBrains, KDevelop4, Kate, Lazarus, LibreOffice, Linux, LyX, Matlab, Mercurial, MicrosoftOffice, ModelSim, Momentics, MonoDevelop, NetBeans, Ninja, NotepadPP, Otto, Redcar, Redis, SBT, SVN, SlickEdit, Stata, SublimeText, SynopsysVCS, Tags, TextMate, TortoiseGit, Vagrant, Vim, VirtualEnv, VisualStudioCode, WebMethods, Windows, Xcode, XilinxISE, macOS
        """.trimIndent()

        println(usage)
    }
}