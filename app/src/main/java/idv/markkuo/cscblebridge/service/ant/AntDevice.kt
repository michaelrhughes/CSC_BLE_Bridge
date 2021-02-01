package idv.markkuo.cscblebridge.service.ant

sealed class AntDevice(val deviceId: Int, val deviceName: String) {
    class BsdDevice(
            id: Int,
            name: String,
            var lastSpeed: Float,
            var cumulativeWheelRevolution: Long,
            var lastWheelEventTime: Int,
            var lastSpeedTimestamp: Long
    ): AntDevice(id, name)

    class SSDevice(
            id: Int,
            name: String,
            var ssDistance: Long,
            var ssDistanceTimestamp: Long,
            var ssSpeed: Float,
            var ssSpeedTimestamp: Long,
            var stridePerMinute: Long,
            var stridePerMinuteTimestamp: Long
    ) : AntDevice(id, name)

    class HRDevice(
            id: Int,
            name: String,
            var hr: Int,
            var hrTimestamp: Long
    ) : AntDevice(id, name)
}