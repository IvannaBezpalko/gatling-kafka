# Gatling-Kafka

Fork an unofficial [Gatling](http://gatling.io/) 2.2.3 stress test plugin
for [Apache Kafka](http://kafka.apache.org/) 0.10 protocol.

This plugin supports the Kafka producer API only
and doesn't support the Kafka consumer API.

### Creating a jar file

Install [sbt](http://www.scala-sbt.org/) 0.13 if you don't have.
And create a jar file:

    $ sbt assembly

If you want to change the version of Gatling used to create a jar file,
change the following line in [`build.sbt`](build.sbt):

```scala
"io.gatling" % "gatling-core" % "2.1.3" % "provided",
```

and run `sbt assembly`.

### Putting the jar file to lib directory

Put the jar file to `lib` directory in Gatling:

    $ cp target/scala-2.11/gatling-kafka-assembly-*.jar /path/to/gatling-charts-highcharts-bundle-2.1.*/lib

If you edited `build.sbt` in order not to include kafka-clients library
to the jar file, you also need to copy kafka-clients library to `lib` directory:

    $ cp /path/to/kafka-clients-*.jar /path/to/gatling-charts-highcharts-bundle-2.1.*/lib


### Running a stress test

After starting an Apache Kafka server, run a stress test:

    $ bin/gatling.sh

## License

Apache License, Version 2.0
