
definition(
    name: "AutoModeChange",
    namespace: "smartthings",
    author: "Thomas",
    description: "Monitors a set of SmartSense Presence tags or smartphones and triggers a mode change to newMode1 when everyone has left. Change mode to newMode2 when any one comes back home",
    category: "Mode Magic",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/App-LightUpMyWorld.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/App-LightUpMyWorld@2x.png"
)
preferences {
	section("When all of these people leave home") {
		input "people", "capability.presenceSensor", multiple: true
	}
	section("Change to this mode") {
		input "newMode1", "mode", title: "Mode?"
	}
	section("Change to this mode when any of them come back home") {
		input "newMode2", "mode", title: "Mode?"
	}

}
def installed() {
	subscribe(people, "presence", presence)
}
def updated() {
	unsubscribe()
	subscribe(people, "presence", presence)
}
def presence(evt)
{
	if (evt.value == "not present") {
		if (location.mode != newMode1) {
			if (everyoneIsAway()) {
				setLocationMode(newMode1)
			}
		}
	}
	else {
		if (location.mode != newMode2) {
			setLocationMode(newMode2)
		}
	}
}
private everyoneIsAway()
{
	def result = true
	for (person in people) {
		if (person.currentPresence == "present") {
			result = false
			break
		}
	}
	return result
}