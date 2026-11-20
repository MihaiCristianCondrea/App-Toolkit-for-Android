Android 17 introduces the `MetricStyle` notifications template to support
health and fitness apps, timers, and travel app use cases.
![Timer example using MetricStyle](https://developer.android.com/static/images/ui/notifications/timer.png)
**Figure 1:** A `MetricStyle` notification template and Live Update for a clock app

## Capabilities

Metric style notifications have the following capabilities:

- Supports using the [
  `setContentTitle`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder#setContentTitle(java.lang.CharSequence))
  method.
- Doesn't show the context text.
- Supports up to 3 [action](https://developer.android.com/reference/android/app/Notification.Action)
  buttons.
- Supports measurement of up to 3 metrics.
    - Each `Notification.Metric` requires a label, value, and an optional unit.
    - Expanded layout appearance varies depending on the number of metrics taken.

![The MetricStyle template in various states](https://developer.android.com/static/images/ui/notifications/metric-style.png)
**Figure 2:** Examples of the `MetricStyle` template in various states. From left to right,
always-on-display (AOD), promoted as a Live Update, default/expanded, and default/collapsed.

## Behavior

The metric style notification changes behaviors based on its state:

- Metric units are appended to the label in the expanded state.
- The collapsed state's second line shows the metrics contents concatenated into a single line. The
  second and third metric appear only if they fit completely when concatenated.
- The unit is omitted in the collapsed state.
- Each metric receives equal horizontal space, regardless of its content.

## As a Live Update

When a metric style notification is promoted to a Live Update, keep in mind the
following considerations:

- No need to provide [
  `Notification.Builder#setContentTitle`](https://developer.android.com/reference/android/app/Notification.Builder#setContentTitle(java.lang.CharSequence))
  because the metric value is used instead. If no title is provided, the app name is shown.
- If [
  `Notification.Builder#setSubtext`](https://developer.android.com/reference/androidx/core/app/NotificationCompat.Builder#setSubText(java.lang.CharSequence))
  is provided, it is shown in the header line, rather than moved to a new line as for other promoted
  styles.
- Action buttons have the same pill visual treatment.

## Key points about the code

- The following is a list of relevant classes and reference documentation:
    - [
      `Notification.MetricStyle`](https://developer.android.com/reference/android/app/Notification.MetricStyle)
    - [
      `Notification.Metric`](https://developer.android.com/reference/android/app/Notification.Metric)
    - [
      `Notification.Metric.FixedFloat`](https://developer.android.com/reference/android/app/Notification.Metric.FixedFloat)
    - [
      `Notification.Metric.FixedInt`](https://developer.android.com/reference/android/app/Notification.Metric.FixedInt)
    - [
      `Notification.Metric.FixedTime`](https://developer.android.com/reference/android/app/Notification.Metric.FixedTime)
    - [
      `Notification.Metric.MetricValue`](https://developer.android.com/reference/android/app/Notification.Metric.MetricValue)
    - [
      `Notification.Metric.TimeDifference`](https://developer.android.com/reference/android/app/Notification.Metric.TimeDifference)

## See also

- [Notification.MetricStyle API reference](https://developer.android.com/reference/android/app/Notification.MetricStyle)
- [Notification.Metric API reference](https://developer.android.com/reference/android/app/Notification.Metric)
- [Notification.Action API reference](https://developer.android.com/reference/android/app/Notification.Action)