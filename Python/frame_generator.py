import cv2
import numpy as np
from PIL import Image
from typing import List, Tuple
import json
import os

class FrameConfig:
    """Configuration for frame generation"""
    
    def __init__(self):
        self.width = 1280
        self.height = 720
        self.fps = 30
        self.duration = 5.0
        self.transition_type = "fade"

class FrameGenerator:
    """Generate and process video frames with effects"""
    
    def __init__(self, config: FrameConfig = None):
        self.config = config or FrameConfig()
    
    def create_transition(self, frame1: np.ndarray, frame2: np.ndarray, 
                         transition_type: str, progress: float) -> np.ndarray:
        """Create transition between two frames"""
        
        if transition_type == "fade":
            return cv2.addWeighted(frame1, 1 - progress, frame2, progress, 0)
        
        elif transition_type == "slide_left":
            width = frame1.shape[1]
            offset = int(width * progress)
            
            transition = np.zeros_like(frame1)
            transition[:, :width-offset] = frame1[:, offset:]
            transition[:, width-offset:] = frame2[:, :offset]
            
            return transition
        
        return frame1
    
    def apply_effects_batch(self, frames: List[np.ndarray], 
                           effects: List[str]) -> List[np.ndarray]:
        """Apply effects to batch of frames"""
        processed_frames = []
        
        for frame in frames:
            processed = frame.copy()
            
            for effect in effects:
                if effect == "blur":
                    processed = cv2.GaussianBlur(processed, (5, 5), 0)
                elif effect == "vignette":
                    h, w = processed.shape[:2]
                    kernel_x = cv2.getGaussianKernel(w, w/3)
                    kernel_y = cv2.getGaussianKernel(h, h/3)
                    kernel = kernel_y * kernel_x.T
                    mask = kernel / kernel.max()
                    
                    for i in range(3):
                        processed[:,:,i] = processed[:,:,i] * mask
            
            processed_frames.append(processed)
        
        return processed_frames
    
    def create_video_from_frames(self, frames: List[np.ndarray], 
                                output_path: str = "output/video.mp4",
                                codec: str = "mp4v") -> bool:
        """Create video from frames using OpenCV"""
        if not frames:
            print("No frames to create video")
            return False
        
        h, w = frames[0].shape[:2]
        fps = self.config.fps
        
        fourcc = cv2.VideoWriter_fourcc(*codec)
        out = cv2.VideoWriter(output_path, fourcc, fps, (w, h))
        
        if not out.isOpened():
            print(f"Failed to create video writer for {output_path}")
            return False
        
        print(f"Creating video: {output_path}, {len(frames)} frames, {fps} fps")
        
        for i, frame in enumerate(frames):
            out.write(frame)
            
            if i % 30 == 0:
                print(f"Written {i}/{len(frames)} frames")
        
        out.release()
        print(f"Video saved: {output_path}")
        return True

if __name__ == "__main__":
    generator = FrameGenerator()
    print("Frame Generator initialized")