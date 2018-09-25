import { ElementDefinition } from "../../../types";

export default [
  {
    type: "JSONWriter",
    category: "WRITER",
    roles: ["writer", "mutator", "stepping", "target"],
    icon: "json.svg"
  },
  {
    type: "BadTextXMLFilterReader",
    category: "READER",
    roles: ["reader", "hasTargets"],
    icon: "stream.svg"
  },
  {
    type: "XMLFragmentParser",
    category: "PARSER",
    roles: ["parser", "hasCode", "simple", "hasTargets", "stepping", "mutator"],
    icon: "xml.svg"
  },
  {
    type: "ElasticIndexingFilter",
    category: "FILTER",
    roles: ["simple", "hasTargets", "target"],
    icon: "ElasticSearch.svg"
  },
  {
    type: "RecordCountFilter",
    category: "FILTER",
    roles: ["hasTargets", "target"],
    icon: "recordCount.svg"
  },
  {
    type: "RollingFileAppender",
    category: "DESTINATION",
    roles: ["destination", "stepping", "target"],
    icon: "files.svg"
  },
  {
    type: "TextWriter",
    category: "WRITER",
    roles: ["hasTargets", "writer", "mutator", "stepping", "target"],
    icon: "text.svg"
  },
  {
    type: "StroomStatsFilter",
    category: "FILTER",
    roles: ["simple", "hasTargets", "target"],
    icon: "StroomStatsStore.svg"
  },
  {
    type: "Reader",
    category: "READER",
    roles: ["reader", "hasTargets", "target"],
    icon: "stream.svg"
  },
  {
    type: "TestFilter",
    category: "INTERNAL",
    roles: ["simple", "hasTargets", "stepping", "target"],
    icon: "stream.svg"
  },
  {
    type: "DSParser",
    category: "PARSER",
    roles: ["parser", "hasCode", "simple", "hasTargets", "stepping", "mutator"],
    icon: "text.svg"
  },
  {
    type: "CombinedParser",
    category: "PARSER",
    roles: ["parser", "hasCode", "simple", "hasTargets", "stepping", "mutator"],
    icon: "text.svg"
  },
  {
    type: "XMLWriter",
    category: "WRITER",
    roles: ["hasTargets", "writer", "mutator", "stepping", "target"],
    icon: "xml.svg"
  },
  {
    type: "Source",
    category: "INTERNAL",
    roles: ["simple", "source", "hasTargets"],
    icon: "stream.svg"
  },
  {
    type: "SplitFilter",
    category: "FILTER",
    roles: ["hasTargets", "target"],
    icon: "split.svg"
  },
  {
    type: "InvalidCharFilterReader",
    category: "READER",
    roles: ["reader", "hasTargets"],
    icon: "stream.svg"
  },
  {
    type: "HttpPostFilter",
    category: "FILTER",
    roles: ["simple", "hasTargets", "target"],
    icon: "stream.svg"
  },
  {
    type: "RollingKafkaAppender",
    category: "DESTINATION",
    roles: ["destination", "stepping", "target"],
    icon: "apache_kafka-icon.svg"
  },
  {
    type: "TestAppender",
    category: "INTERNAL",
    roles: ["destination", "target"],
    icon: "stream.svg"
  },
  {
    type: "KafkaAppender",
    category: "DESTINATION",
    roles: ["destination", "stepping", "target"],
    icon: "apache_kafka-icon.svg"
  },
  {
    type: "XSLTFilter",
    category: "FILTER",
    roles: ["hasCode", "simple", "hasTargets", "stepping", "mutator", "target"],
    icon: "xslt.svg"
  },
  {
    type: "StreamAppender",
    category: "DESTINATION",
    roles: ["destination", "stepping", "target"],
    icon: "stream.svg"
  },
  {
    type: "RecordOutputFilter",
    category: "FILTER",
    roles: ["hasTargets", "target"],
    icon: "recordOutput.svg"
  },
  {
    type: "XMLParser",
    category: "PARSER",
    roles: ["parser", "simple", "hasTargets", "stepping", "mutator"],
    icon: "xml.svg"
  },
  {
    type: "HDFSFileAppender",
    category: "DESTINATION",
    roles: ["destination", "stepping", "target"],
    icon: "hadoop-elephant-logo.svg"
  },
  {
    type: "SearchResultOutputFilter",
    category: "FILTER",
    roles: ["hasTargets", "target"],
    icon: "search.svg"
  },
  {
    type: "FileAppender",
    category: "DESTINATION",
    roles: ["destination", "stepping", "target"],
    icon: "file.svg"
  },
  {
    type: "ReferenceDataFilter",
    category: "FILTER",
    roles: ["hasTargets", "target"],
    icon: "referenceData.svg"
  },
  {
    type: "InvalidXMLCharFilterReader",
    category: "READER",
    roles: ["reader", "hasTargets"],
    icon: "stream.svg"
  },
  {
    type: "IdEnrichmentFilter",
    category: "FILTER",
    roles: ["hasTargets", "stepping", "mutator", "target"],
    icon: "id.svg"
  },
  {
    type: "HTTPAppender",
    category: "DESTINATION",
    roles: ["destination", "stepping", "target"],
    icon: "stream.svg"
  },
  {
    type: "SchemaFilter",
    category: "FILTER",
    roles: ["validator", "hasTargets", "stepping", "target"],
    icon: "xsd.svg"
  },
  {
    type: "IndexingFilter",
    category: "FILTER",
    roles: ["simple", "hasTargets", "target"],
    icon: "index.svg"
  },
  {
    type: "BOMRemovalFilterInput",
    category: "READER",
    roles: ["reader", "hasTargets"],
    icon: "stream.svg"
  },
  {
    type: "StroomStatsAppender",
    category: "DESTINATION",
    roles: ["destination", "stepping", "target"],
    icon: "StroomStatsStore.svg"
  },
  {
    type: "RollingStreamAppender",
    category: "DESTINATION",
    roles: ["destination", "stepping", "target"],
    icon: "stream.svg"
  },
  {
    type: "JSONParser",
    category: "PARSER",
    roles: ["parser", "simple", "hasTargets", "stepping", "mutator"],
    icon: "json.svg"
  },
  {
    type: "GenericKafkaProducerFilter",
    category: "FILTER",
    roles: ["simple", "hasTargets", "target"],
    icon: "apache_kafka-icon.svg"
  }
] as Array<ElementDefinition>;
