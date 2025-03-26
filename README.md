<a href="https://github.com/encalmo/scala-aws-secrets">![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)</a> <a href="https://central.sonatype.com/artifact/org.encalmo/scala-aws-secrets_3" target="_blank">![Maven Central Version](https://img.shields.io/maven-central/v/org.encalmo/scala-aws-secrets_3?style=for-the-badge)</a> <a href="https://encalmo.github.io/scala-aws-secrets/scaladoc/org/encalmo/aws.html" target="_blank"><img alt="Scaladoc" src="https://img.shields.io/badge/docs-scaladoc-red?style=for-the-badge"></a>

# scala-aws-secrets

This Scala 3 library provides a helper to access variables stored in AWS SecretsManager.

## Table of contents

- [Dependencies](#dependencies)
- [Usage](#usage)
- [Examples](#examples)
- [Project content](#project-content)

## Dependencies

   - [Scala](https://www.scala-lang.org) >= 3.3.5
   - [Scala **toolkit** 0.7.0](https://github.com/scala/toolkit)
   - software.amazon.awssdk [**regions** 2.30.32](https://central.sonatype.com/artifact/software.amazon.awssdk/regions) | [**secretsmanager** 2.30.32](https://central.sonatype.com/artifact/software.amazon.awssdk/secretsmanager) | [**url-connection-client** 2.30.31](https://central.sonatype.com/artifact/software.amazon.awssdk/url-connection-client)

## Usage

Use with SBT

    libraryDependencies += "org.encalmo" %% "scala-aws-secrets" % "0.9.0"

or with SCALA-CLI

    //> using dep org.encalmo::scala-aws-secrets:0.9.0

## Examples


## Project content

```
├── .github
│   └── workflows
│       ├── pages.yaml
│       ├── release.yaml
│       └── test.yaml
│
├── .gitignore
├── .scalafmt.conf
├── LambdaSecrets.scala
├── LambdaSecrets.test.scala
├── LICENSE
├── project.scala
├── README.md
└── test.sh
```

