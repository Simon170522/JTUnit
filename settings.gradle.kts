rootProject.name = "JTUnit"
include("modules:testEngine")
findProject(":modules:testEngine")?.name = "testEngine"
