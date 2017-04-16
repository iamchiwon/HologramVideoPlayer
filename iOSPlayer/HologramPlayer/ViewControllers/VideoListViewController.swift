//
//  VideoListViewController.swift
//  HologramPlayer
//
//  Created by Ryan Song on 2017. 4. 16..
//  Copyright © 2017년 makecube. All rights reserved.
//

import UIKit
import Photos

class VideoListViewController: UIViewController {
  
  @IBOutlet weak var collectionView: UICollectionView!
  
  var videos = PHFetchResult<PHAsset>()
  var manager = PHImageManager.default()
  
  lazy var cellSize : CGSize = {
    let screenWidth = UIScreen.main.bounds.size.width
    var cellSize : CGFloat = 150.0
    
    switch(screenWidth) {
    case let w where w < 350 : //iphone 4S, SE
      cellSize = (screenWidth - 5 * 3) / 2
      
    case let w where w < 500 : //iphone 7, 7+
      cellSize = (screenWidth - 5 * 4) / 3
      
    case let w where w < 900 : //iPad  9.7"
      cellSize = (screenWidth - 10 * 5) / 4
      
    default : //iPad 12.9"
      cellSize = (screenWidth - 10 * 6) / 5
    }
    
    return CGSize(width: cellSize, height: cellSize)
  }()
  
  lazy var cellGap :CGFloat = {
    let screenWidth = UIScreen.main.bounds.size.width
    
    switch(screenWidth) {
    case let w where w < 500 : //iphone 4S, SE, 7, 7+
      return 5
    default : //iPad  9.7", 12.9"
      return 10
    }
  }()
  
  override func viewDidLoad() {
    super.viewDidLoad()
    navigationController?.isNavigationBarHidden = false
    collectionView.delegate = self
  }
  
  override func viewWillLayoutSubviews() {
    super.viewWillLayoutSubviews()
    collectionView.collectionViewLayout.invalidateLayout()
  }
  
  override func viewWillAppear(_ animated: Bool) {
    super.viewWillAppear(animated)
    
    let options = PHFetchOptions()
    let sortByCreateDate = NSSortDescriptor(key: "creationDate", ascending: false)
    options.sortDescriptors = [sortByCreateDate]
    videos = PHAsset.fetchAssets(with: .video, options: options)
    collectionView.reloadData()
  }
  
  override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
  }
  
  @IBAction func onCaptureNewVideo(_ sender: Any) {
    print("Capture new video")
  }
  
}

extension VideoListViewController : UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
  
  func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
    return videos.count
  }
  
  func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
    let cell = collectionView .dequeueReusableCell(withReuseIdentifier: "VideoCell", for: indexPath);
    
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
