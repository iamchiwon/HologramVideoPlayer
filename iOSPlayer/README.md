## [iOS Player](iOSPlayer/README.md)

### 비디오 재생하기

- AVPlayer 를 사용해서 재생시킴
- AVPlayerLayer 를 4개 생성하고 모두 동일한 AVPlayer 를 사용하도록 함
- 하나의 AVPlayer 에서 파생된 Layer들 이므로 동기화가 필요없음

### 비디오 회전

- UIView 의 layer 에 AVPlayerLayer 를 추가하고
- UIView에 transform 시켜서 rotate, flip 시킴

```swift
func flipVideo() {
    if standupVideo {
      player1.transform = CGAffineTransform(scaleX: 1, y: -1)
      player2.transform = CGAffineTransform(rotationAngle: 0.5 * .pi)
      player3.transform = CGAffineTransform(scaleX: 1, y: -1).rotated(by: -0.5 * .pi)
      player4.transform = CGAffineTransform(scaleX: 1, y: 1)
    } else {
      player1.transform = CGAffineTransform(scaleX: -1, y: 1)
      player2.transform = CGAffineTransform(rotationAngle: -0.5 * .pi)
      player3.transform = CGAffineTransform(scaleX: 1, y: -1).rotated(by: 0.5 * .pi)
      player4.transform = CGAffineTransform(rotationAngle: 1.0 * .pi)
    }
  }
```

### 스크린샷

![screenshot](../images/screenshot2.png)