$version: "2"

namespace cli

@alloy#simpleRestJson
service Numbers {
    operations: [
        GetNumberFact
    ]
}

@readonly
@http(method: "GET", uri: "/{number}/{type}")
operation GetNumberFact {
    input := {
        @required
        @pattern("^(([0-9]*)|(random))$")
        @httpLabel
        number: String

        @required
        @httpLabel
        type: Type

        @required
        @httpQuery("json")
        json: Boolean = true
    }

    output := {
        @required
        text: String

        @required
        found: Boolean
    }
}

enum Type {
    TRIVIA = "trivia"
    MATH = "math"
    DATE = "date"
    YEAR = "year"
}
