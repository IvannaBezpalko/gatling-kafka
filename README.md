# Gatling-Kafka

Fork an unofficial [Gatling](http://gatling.io/) 2.1 stress test plugin
for [Apache Kafka](http://kafka.apache.org/) 0.8 protocol.

This plugin supports the Kafka producer API only
and doesn't support the Kafka consumer API.

## Usage

### Cloning this repository

    $ git clone https://github.com/mnogu/gatling-kafka.git
    $ cd gatling-kafka

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

If you don't want to include kafka-clients library to the jar file,
change a line on kafka-clients in [`build.sbt`](build.sbt) from

```scala
("org.apache.kafka" % "kafka-clients" % "0.8.2.0")
```

to

```scala
("org.apache.kafka" % "kafka-clients" % "0.8.2.0" % "provided")
```

and run `sbt assembly`.

Note that Apache Kafka 0.8.1.1 or below doesn't contain kafka-clients library.

### Putting the jar file to lib directory

Put the jar file to `lib` directory in Gatling:

    $ cp target/scala-2.11/gatling-kafka-assembly-*.jar /path/to/gatling-charts-highcharts-bundle-2.1.*/lib

If you edited `build.sbt` in order not to include kafka-clients library
to the jar file, you also need to copy kafka-clients library to `lib` directory:

    $ cp /path/to/kafka-clients-*.jar /path/to/gatling-charts-highcharts-bundle-2.1.*/lib


###  Creating a simulation file

You can find sample simulation files in the [test directory](src/test/scala/com/github/mnogu/gatling/kafka/test).
Among these files, [BasicSimulation.scala](src/test/scala/com/github/mnogu/gatling/kafka/test/BasicSimulation.scala) would be a good start point.
Make sure that you replace `BasicSimulation` with `KafkaSimulation` in `BasicSimulation.scala`
if your simulation filename is `KafkaSimulation.scala`.

### Running a stress test

After starting an Apache Kafka server, run a stress test:

    $ bin/gatling.sh

## License

Apache License, Version 2.0
