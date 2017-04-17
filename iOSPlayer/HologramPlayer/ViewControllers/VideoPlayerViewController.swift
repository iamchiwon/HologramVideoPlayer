//
//  VideoPlayerViewController.swift
//  HologramPlayer
//
//  Created by Ryan Song on 2017. 4. 16..
//  Copyright © 2017년 makecube. All rights reserved.
//

import UIKit
import Photos
import AVKit
import AVFoundation

class VideoPlayerViewController: UIViewController {

  @IBOutlet weak var bottomControlBox: UIVisualEffectView!
  @IBOutlet weak var bottomBoxBottomConstrains: NSLayoutConstraint!
  @IBOutlet weak var player1: UIView!
  @IBOutlet weak var player2: UIView!
  @IBOutlet weak var player3: UIView!
  @IBOutlet weak var player4: UIView!

  var layer1: AVPlayerLayer!
  var layer2: AVPlayerLayer!
  var layer3: AVPlayerLayer!
  var layer4: AVPlayerLayer!

  var videoAsset: PHAsset!
  var videoPlayer: AVPlayer! = nil

  var standupVideo: Bool = true

  override func viewDidLoad() {
    super.viewDidLoad()

    flipVideo()

    let tapper = UITapGestureRecognizer(target: self, action: #selector(onTap))
    self.view.addGestureRecognizer(tapper)
    bottomControlBox.isHidden = true
  }

  override var prefersStatusBarHidden: Bool {
    return true
  }

  override func viewWillAppear(_ animated: Bool) {
    super.viewWillAppear(animated)
    navigationController?.isNavigationBarHidden = true
  }

  override func viewDidAppear(_ animated: Bool) {
    super.viewDidAppear(animated)
    prepareVideo()
  }

  override func viewWillDisappear(_ animated: Bool) {
    super.viewWillDisappear(animated)
    videoPlayer.pause()
  }

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

  func prepareVideo() {
    let options: PHVideoRequestOptions = PHVideoRequestOptions()
    PHImageManager.default().requestAVAsset(forVideo: videoAsset, options: options) { (asset, mix, info) in
      if let urlAsset = asset as? AVURLAsset {
        let localVideoUrl = urlAsset.url
        DispatchQueue.main.async {
          self.videoPlayer = AVPlayer(url: localVideoUrl)
          self.prepareVideoLayer()
          self.playVideo()
        }
      }
    }
  }

  func prepareVideoLayer() {
    self.layer1 = AVPlayerLayer(player: self.videoPlayer)
    self.layer2 = AVPlayerLayer(player: self.videoPlayer)
    self.layer3 = AVPlayerLayer(player: self.videoPlayer)
    self.layer4 = AVPlayerLayer(player: self.videoPlayer)

    self.layer1.frame = self.player1.bounds
    self.layer2.frame = self.player2.bounds
    self.layer3.frame = self.player3.bounds
    self.layer4.frame = self.player4.bounds

    self.player1.layer.addSublayer(self.layer1)
    self.player2.layer.addSublayer(self.layer2)
    self.player3.layer.addSublayer(self.layer3)
    self.player4.layer.addSublayer(self.layer4)
  }

  func playVideo() {
    self.videoPlayer.play()

    NotificationCenter.default.addObserver(forName: .AVPlayerItemDidPlayToEndTime, object: self.videoPlayer.currentItem, queue: nil, using: { (_) in
      DispatchQueue.main.async {
        self.videoPlayer?.seek(to: kCMTimeZero)
        self.videoPlayer?.play()
      }
    })
  }

  func onTap() {
    bottomControlBox.isHidden = !bottomControlBox.isHidden
  }

  @IBAction func onClose(_ sender: Any) {
    navigationController?.popViewController(animated: true)
  }

  @IBAction func onFlip(_ sender: Any) {
    standupVideo = !standupVideo
    flipVideo()
  }

  @IBAction func onPause(_ sender: UIButton) {
    if sender.title(for: .normal) == "Pause" {
      videoPlayer.pause()
      sender.setTitle("Play", for: .normal)
    } else {
      videoPlayer.play()
      sender.setTitle("Pause", for: .normal)
    }
  }
  
  @IBAction func onMute(_ sender: Any) {
    videoPlayer.isMuted = !videoPlayer.isMuted
  }

}
