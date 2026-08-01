#include <opencv2/opencv.hpp>
#include <iostream>
#include <vector>
#include <chrono>
#include <thread>
#include <atomic>
#include <memory>

class VideoEncoder {
private:
    std::string outputPath;
    int fps;
    cv::Size frameSize;
    std::atomic<int> frameCounter{0};
    
public:
    VideoEncoder(const std::string& path, int fps = 30, cv::Size size = cv::Size(1280, 720))
        : outputPath(path), fps(fps), frameSize(size) {}
    
    bool encode(const std::vector<cv::Mat>& frames) {
        if (frames.empty()) {
            std::cerr << "No frames to encode!" << std::endl;
            return false;
        }
        
        cv::VideoWriter writer(outputPath, 
                              cv::VideoWriter::fourcc('H', '2', '6', '4'), 
                              fps, frameSize);
        
        if (!writer.isOpened()) {
            std::cerr << "Failed to open video writer!" << std::endl;
            return false;
        }
        
        std::cout << "Encoding video with " << frames.size() << " frames..." << std::endl;
        
        for (const auto& frame : frames) {
            writer.write(frame);
            frameCounter++;
            
            if (frameCounter % 10 == 0) {
                std::cout << "Frames encoded: " << frameCounter << std::endl;
            }
        }
        
        writer.release();
        std::cout << "Video saved to: " << outputPath << std::endl;
        return true;
    }
};

int main() {
    std::cout << "Video Encoder Initialized!" << std::endl;
    return 0;
}