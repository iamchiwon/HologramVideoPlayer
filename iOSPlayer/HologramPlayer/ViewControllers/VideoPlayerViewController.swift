//
//  VideoPlayerViewController.swift
//  HologramPlayer
//
//  Created by Ryan Song on 2017. 4. 16..
//  Copyright © 2017년 makecube. All rights reserved.
//

import UIKit

class VideoPlayerViewController: UIViewController {
  
  @IBOutlet weak var bottomControlBox: UIVisualEffectView!
  @IBOutlet weak var bottomBoxBottomConstrains: NSLayoutConstraint!
  
  override func viewDidLoad() {
    super.viewDidLoad()
    
    navigationController?.isNavigationBarHidden = true
    
    let tapper = UITapGestureRecognizer(target: self, action: #selector(onTap))
    self.view.addGestureRecognizer(tapper)
  }
  
  func onTap() {
    showControlBox()
  }
  
  override func viewDidAppear(_ animated: Bool) {
    showControlBox();
  }
  
  func showControlBox() {
    bottomControlBox.isHidden = false
    self.bottomBoxBottomConstrains.constant = 0
    self.view.layoutIfNeeded()
    
    /*
    UIView.animate(withDuration: 0.3,
                   delay: 1.7,
                   usingSpringWithDamping: 0.5,
                   initialSpringVelocity: 0.7,
                   options: UIViewAnimationOptions.curveEaseInOut,
                   animations: {
                    let height = self.bottomControlBox.bounds.size.height
                    self.bottomBoxBottomConstrains.constant = -height
                    self.view.layoutIfNeeded()
    }) { (completion) in
      self.bottomControlBox.isHidden = true
    }*/
  }
  
  @IBAction func onClose(_ sender: Any) {
    navigationController?.popViewController(animated: true)
  }
  
}
