rootProject.name = "catching"

// domain
include("domain")
include("domain-model")

// data
include("data-rds")
include("data-redis")

// client
include("client-member")

// application
include("admin")
include("batch")
include("external")

// test-support
include("test-support")
