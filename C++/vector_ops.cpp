#include <immintrin.h>
#include <cmath>
#include <vector>
#include <iostream>

class SIMDVectorOps {
public:
    static void addVectors(float* result, const float* a, const float* b, size_t n) {
        size_t i = 0;
        
        for (; i + 8 <= n; i += 8) {
            __m256 av = _mm256_loadu_ps(a + i);
            __m256 bv = _mm256_loadu_ps(b + i);
            __m256 rv = _mm256_add_ps(av, bv);
            _mm256_storeu_ps(result + i, rv);
        }
        
        for (; i < n; i++) {
            result[i] = a[i] + b[i];
        }
    }
    
    static void multiplyVectors(float* result, const float* a, const float* b, size_t n) {
        size_t i = 0;
        
        for (; i + 8 <= n; i += 8) {
            __m256 av = _mm256_loadu_ps(a + i);
            __m256 bv = _mm256_loadu_ps(b + i);
            __m256 rv = _mm256_mul_ps(av, bv);
            _mm256_storeu_ps(result + i, rv);
        }
        
        for (; i < n; i++) {
            result[i] = a[i] * b[i];
        }
    }
    
    static float dotProduct(const float* a, const float* b, size_t n) {
        __m256 sum = _mm256_setzero_ps();
        size_t i = 0;
        
        for (; i + 8 <= n; i += 8) {
            __m256 av = _mm256_loadu_ps(a + i);
            __m256 bv = _mm256_loadu_ps(b + i);
            sum = _mm256_add_ps(sum, _mm256_mul_ps(av, bv));
        }
        
        float result = 0;
        for (; i < n; i++) {
            result += a[i] * b[i];
        }
        
        return result;
    }
};

int main() {
    std::cout << "Vector Operations (SIMD) Initialized!" << std::endl;
    return 0;
}