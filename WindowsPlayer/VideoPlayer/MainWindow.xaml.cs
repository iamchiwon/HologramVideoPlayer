using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Concurrency;
using System.Reactive.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;

namespace HologramPlayer
{
    /// <summary>
    /// MainWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            SetUpVideoPlayer();
            RotateUpstand();
        }

        private void OnLoaded(object sender, RoutedEventArgs e)
        {
            //EnterFullScreenMode();
        }

        string videoFileName = "";

        private void OpenMediaFile()
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "VIDEO files (*.avi;*.wmv;*.mp4)|*.avi;*.wmv;*.mp4|All files (*.*)|*.*";
            if (openFileDialog.ShowDialog() == true)
            {
                videoFileName = openFileDialog.FileName;
                SetVideoPlayer(videoFileName);
            }
        }

        IDisposable timerDisposable = null;

        private void DoSync()
        {
            StopSync();

            timerDisposable = Observable.Interval(TimeSpan.FromMilliseconds(500))
                .ObserveOnDispatcher()
                .Subscribe(aLong =>
                {
                    RunSync();
                });
        }

        private void StopSync()
        {
            if (timerDisposable != null)
            {
                timerDisposable.Dispose();
                timerDisposable = null;
            }
        }

        private void RunSync()
        {
            MediaElement[] players = new MediaElement[] { player2, player3, player4 };
            players.ToObservable().Subscribe(p =>
            {
                double standard = player1.Position.TotalMilliseconds;
                double target = p.Position.TotalMilliseconds;
                double diff = standard - target;
                if (-2 < diff && diff < 2)
                {
                    p.SpeedRatio = 1;
                }
                else
                {
                    if (standard < target) p.SpeedRatio = 0.9;
                    else p.SpeedRatio = 1.1;
                }
            });
        }

        private void SetUpVideoPlayer()
        {
            MediaElement[] players = new MediaElement[] { player1, player2, player3, player4 };

            foreach(MediaElement p in players)
            {
                p.IsMuted = true;
                p.LoadedBehavior = MediaState.Manual;
                //p.UnloadedBehavior = MediaState.Manual;
            }
            player1.IsMuted = false;
            player1.MediaEnded += new RoutedEventHandler(VideoPlayEnded);
        }

        private void RotateUpstand()
        {
            ScaleTransform V_Flip = new ScaleTransform();
            V_Flip.ScaleY = -1;
            V_Flip.ScaleX = 1;
            ScaleTransform H_Flip = new ScaleTransform();
            H_Flip.ScaleY = 1;
            H_Flip.ScaleX = -1;
            RotateTransform rotateRight = new RotateTransform();
            rotateRight.Angle = 90;
            RotateTransform rotateLeft = new RotateTransform();
            rotateLeft.Angle = -90;

            TransformGroup player1Transform = new TransformGroup();
            player1Transform.Children.Add(V_Flip);
            player1.RenderTransform = player1Transform;

            TransformGroup player2Transform = new TransformGroup();
            player2Transform.Children.Add(rotateRight);
            player2.RenderTransform = player2Transform;

            TransformGroup player3Transform = new TransformGroup();
            player3Transform.Children.Add(H_Flip);
            player3Transform.Children.Add(rotateLeft);
            player3.RenderTransform = player3Transform;
            
            player4.RenderTransform = null;
        }

        private void RotateDownstand()
        {
            ScaleTransform V_Flip = new ScaleTransform();
            V_Flip.ScaleY = -1;
            V_Flip.ScaleX = 1;
            ScaleTransform H_Flip = new ScaleTransform();
            H_Flip.ScaleY = 1;
            H_Flip.ScaleX = -1;
            RotateTransform rotateRight = new RotateTransform();
            rotateRight.Angle = 90;
            RotateTransform rotateLeft = new RotateTransform();
            rotateLeft.Angle = -90;

            TransformGroup player1Transform = new TransformGroup();
            player1Transform.Children.Add(H_Flip);
            player1.RenderTransform = player1Transform;

            TransformGroup player2Transform = new TransformGroup();
            player2Transform.Children.Add(rotateLeft);
            player2.RenderTransform = player2Transform;

            TransformGroup player3Transform = new TransformGroup();
            player3Transform.Children.Add(H_Flip);
            player3Transform.Children.Add(rotateRight);
            player3.RenderTransform = player3Transform;

            TransformGroup player4Transform = new TransformGroup();
            player4Transform.Children.Add(V_Flip);
            player4Transform.Children.Add(H_Flip);
            player4.RenderTransform = player4Transform;
        }

        private void SetVideoPlayer(string filename)
        {
            StopSync();

            MediaElement[] players = new MediaElement[] { player1, player2, player3, player4 };
            players.ToObservable().Subscribe(p =>
            {
                p.Source = new Uri(filename, UriKind.Absolute);
                p.Play();
            }, () =>
            {
                DoSync();
            });
        }

        bool isPaused = false;
        private void PauseVideo()
        {
            isPaused = !isPaused;
            MediaElement[] players = new MediaElement[] { player1, player2, player3, player4 };

            if (isPaused)
            {
                players.ToObservable().Subscribe(p =>
                {
                    p.Pause();
                });
            }
            else
            {
                players.ToObservable().Subscribe(p =>
                {
                    p.Play();
                });
            }
        }

        private void VideoPlayEnded(object sender, RoutedEventArgs e)
        {
            StopSync();
            MediaElement[] players = new MediaElement[] { player1, player2, player3, player4 };
            players.ToObservable().Subscribe(p =>
            {
                p.Stop();
                p.Position = TimeSpan.FromMilliseconds(1);
                p.Play();
            }, () =>
            {
                DoSync();
            });
        }

        private void KeyboardDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                if (IsFullScreen) ExitFullScreenMode(); else EnterFullScreenMode();
            }
            else if(e.Key == Key.O)
            {
                OpenMediaFile();
            }
            else if(e.Key == Key.Space)
            {
                PauseVideo();
            }
            else if(e.Key == Key.M)
            {
                player1.IsMuted = !player1.IsMuted;
            }
            else if(e.Key == Key.D1)
            {
                RotateUpstand();
            }
            else if (e.Key == Key.D2)
            {
                RotateDownstand();
            }
        }

        bool IsFullScreen = false;

        private void EnterFullScreenMode()
        {
            Application.Current.MainWindow.WindowStyle = System.Windows.WindowStyle.None;
            Application.Current.MainWindow.WindowState = System.Windows.WindowState.Maximized;
            Application.Current.MainWindow.Topmost = true;

            IsFullScreen = true;
        }

        private void ExitFullScreenMode()
        {
            Application.Current.MainWindow.WindowStyle = System.Windows.WindowStyle.SingleBorderWindow;
            Application.Current.MainWindow.WindowState = System.Windows.WindowState.Normal;
            Application.Current.MainWindow.Topmost = false;

            IsFullScreen = false;
        }

        private void MouseDoubleClicked(object sender, MouseButtonEventArgs e)
        {
            if (IsFullScreen) ExitFullScreenMode(); else EnterFullScreenMode();
        }
    }
}
