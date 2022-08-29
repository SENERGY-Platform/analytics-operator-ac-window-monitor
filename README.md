# operator-ac-window-monitor

Takes values from devices with same service and adds them together returning the sum of all last messages (see example). Sets the timestamp to the one from the last message

## Inputs

* value (float): Reading from device
* timestamp (string): Timestamp from device

## Outputs

* sum (float): Sum of all latest values
* lastTimestamp (string): timestamp of last message

## Example


| Device | value | timestamp              | sum | lastTimestamp          |
|--------|-------|------------------------|-----|------------------------|
| A      | 5     | 2000-01-01T10:25:13+02 | 5   | 2000-01-01T10:25:13+02 |
| B      | 13    | 2000-01-01T18:37:11+04 | 8   | 2000-01-01T18:37:11+04 |
| A      | 7     | 2001-03-04T12:14:54+02 | 20  | 2001-03-04T12:14:54+02 |
