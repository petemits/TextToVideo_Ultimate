#include <iostream>
#include <vector>
#include <memory>
#include <unordered_map>
#include <mutex>

class MemoryPool {
private:
    struct MemoryBlock {
        void* ptr;
        size_t size;
        bool inUse;
    };
    
    std::vector<MemoryBlock> blocks;
    std::mutex poolMutex;
    
public:
    MemoryPool() {
        blocks.reserve(100);
    }
    
    ~MemoryPool() {
        clear();
    }
    
    void* allocate(size_t size) {
        std::lock_guard<std::mutex> lock(poolMutex);
        
        for (auto& block : blocks) {
            if (!block.inUse && block.size >= size) {
                block.inUse = true;
                return block.ptr;
            }
        }
        
        void* newPtr = malloc(size);
        if (newPtr) {
            blocks.push_back({newPtr, size, true});
        }
        
        return newPtr;
    }
    
    void deallocate(void* ptr) {
        std::lock_guard<std::mutex> lock(poolMutex);
        
        for (auto& block : blocks) {
            if (block.ptr == ptr && block.inUse) {
                block.inUse = false;
                break;
            }
        }
    }
    
    void clear() {
        std::lock_guard<std::mutex> lock(poolMutex);
        
        for (auto& block : blocks) {
            free(block.ptr);
        }
        blocks.clear();
    }
    
    size_t getUsage() const {
        size_t used = 0;
        for (const auto& block : blocks) {
            if (block.inUse) used += block.size;
        }
        return used;
    }
};

int main() {
    std::cout << "Memory Manager Initialized!" << std::endl;
    return 0;
}