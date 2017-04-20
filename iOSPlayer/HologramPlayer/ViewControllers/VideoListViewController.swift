//
//  VideoListViewController.swift
//  HologramPlayer
//
//  Created by Ryan Song on 2017. 4. 16..
//  Copyright © 2017년 makecube. All rights reserved.
//

import UIKit
import Photos
import MobileCoreServices

class VideoListViewController: UIViewController {

  @IBOutlet weak var collectionView: UICollectionView!
  @IBOutlet weak var captureVideo: UIBarButtonItem!

  var videos = PHFetchResult<PHAsset>()
  var manager = PHImageManager.default()

  lazy var cellSize: CGSize = {
    let screenWidth = UIScreen.main.bounds.size.width
    var cellSize: CGFloat = 150.0

    switch(screenWidth) {
    case let w where w < 350: //iphone 4S, SE
      cellSize = (screenWidth - 5 * 3) / 2

    case let w where w < 500: //iphone 7, 7+
      cellSize = (screenWidth - 5 * 4) / 3

    case let w where w < 900: //iPad  9.7"
      cellSize = (screenWidth - 10 * 5) / 4

    default: //iPad 12.9"
      cellSize = (screenWidth - 10 * 6) / 5
    }

    return CGSize(width: cellSize, height: cellSize)
  }()

  lazy var cellGap: CGFloat = {
    let screenWidth = UIScreen.main.bounds.size.width

    switch(screenWidth) {
    case let w where w < 500: //iphone 4S, SE, 7, 7+
      return 5
    default: //iPad  9.7", 12.9"
      return 10
    }
  }()

  override func viewDidLoad() {
    super.viewDidLoad()
    captureVideo.isEnabled = UIImagePickerController.isSourceTypeAvailable(.camera)
    collectionView.delegate = self
  }

  override var prefersStatusBarHidden: Bool {
    return false
  }

  override func viewWillLayoutSubviews() {
    super.viewWillLayoutSubviews()
    collectionView.collectionViewLayout.invalidateLayout()
  }

  override func viewWillAppear(_ animated: Bool) {
    super.viewWillAppear(animated)
    navigationController?.isNavigationBarHidden = false
  }

  override func viewDidAppear(_ animated: Bool) {
    super.viewDidAppear(animated)

    let status = PHPhotoLibrary.authorizationStatus()
    if(status == .authorized) {
      loadVideos()
    } else if(status == .notDetermined) {
      PHPhotoLibrary.requestAuthorization({ (result) in
        if(result == .authorized) {
          self.loadVideos()
        } else {
          self.showNeedAuthorizationMessage()
        }
      })
    } else {
      showNeedAuthorizationMessage()
    }
  }

  override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
    if let identifier = segue.identifier, identifier == "PlayVideoSegue" {
      if let indexPath = collectionView.indexPathsForSelectedItems?[0] {
        let asset = videos.object(at: indexPath.row)
        let destination = segue.destination as! VideoPlayerViewController
        destination.videoAsset = asset
      }
    }
  }
  
  func showNeedAuthorizationMessage() {
    let label = UILabel()
    label.text = "Please check to see if device settings doesn't allow photo library access."
    label.textColor = .darkGray
    label.numberOfLines = 0
    label.textAlignment = .center
    view.addSubview(label)
    
    label.translatesAutoresizingMaskIntoConstraints = false
    label.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
    label.centerYAnchor.constraint(equalTo: view.centerYAnchor).isActive = true
    label.widthAnchor.constraint(equalTo: view.widthAnchor, multiplier: 0.8).isActive = true
    
    let button = UIButton()
    button.setTitle("Settings", for: .normal)
    button.setTitleColor(.blue, for: .normal)
    view.addSubview(button)
    
    button.translatesAutoresizingMaskIntoConstraints = false
    button.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
    button.topAnchor.constraint(equalTo: label.bottomAnchor, constant: 30).isActive = true
    
    button.addTarget(self, action: #selector(goSetting), for: .touchUpInside)
  }
  
  func goSetting() {
    let settingsUrl = NSURL(string:UIApplicationOpenSettingsURLString)
    if let url = settingsUrl {
      UIApplication.shared.openURL(url as URL)
    }
  }

  func loadVideos() {
    let options = PHFetchOptions()
    let sortByCreateDate = NSSortDescriptor(key: "creationDate", ascending: false)
    options.sortDescriptors = [sortByCreateDate]
    videos = PHAsset.fetchAssets(with: .video, options: options)
    collectionView.reloadData()
  }

}

extension VideoListViewController: UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {

  func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return videos.count
  }

  func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "VideoCell", for: indexPath);

    if let imageView = cell.viewWithTag(1) as! UIImageView? {
      let asset = videos.object(at: indexPath.row)
      manager.requestImage(for: asset, targetSize: cellSize, contentMode: .aspectFill, options: nil, resultHandler: { (image, info) in
        imageView.image = image
      })
    }

    return cell
  }

  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      sizeForItemAt indexPath: IndexPath) -> CGSize {
    return cellSize
  }

  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
    return cellGap
  }

  func collectionView(_ collectionView: UICollectionView,
                      layout collectionViewLayout: UICollectionViewLayout,
                      minimumLineSpacingForSectionAt section: Int) -> CGFloat {
    return cellGap
  }
}

extension VideoListViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {

  @IBAction func onCaptureNewVideo(_ sender: Any) {
    let imagePickerController = UIImagePickerController()
    imagePickerController.sourceType = .camera
    imagePickerController.allowsEditing = true
    imagePickerController.mediaTypes = [kUTTypeMovie as String]
    imagePickerController.showsCameraControls = true
    imagePickerController.delegate = self

    present(imagePickerController, animated: true)
  }

  func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String: Any]) {
    let urlOfVideo = info[UIImagePickerControllerMediaURL] as? NSURL
    if let videoPath = urlOfVideo?.path {
      if UIVideoAtPathIsCompatibleWithSavedPhotosAlbum(videoPath) {
        UISaveVideoAtPathToSavedPhotosAlbum(videoPath, self, #selector(video(videoPath: didFinishSavingWithError: contextInfo:)), nil)
      }
    }

    picker.dismiss(animated: true)
  }

  func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
    picker.dismiss(animated: true)
  }

  func video(videoPath: NSString, didFinishSavingWithError error: NSError?, contextInfo info: AnyObject) {
    if error == nil {
      loadVideos()
    }
  }

}
