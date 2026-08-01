#pragma once
#include <cmath>
#include <vector>

#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif

struct Vector3 {
    float x, y, z;
    
    Vector3() : x(0), y(0), z(0) {}
    Vector3(float x, float y, float z) : x(x), y(y), z(z) {}
    
    Vector3 operator+(const Vector3& other) const {
        return Vector3(x + other.x, y + other.y, z + other.z);
    }
    
    Vector3 operator-(const Vector3& other) const {
        return Vector3(x - other.x, y - other.y, z - other.z);
    }
    
    Vector3 operator*(float scalar) const {
        return Vector3(x * scalar, y * scalar, z * scalar);
    }
    
    float length() const {
        return std::sqrt(x*x + y*y + z*z);
    }
    
    Vector3 normalized() const {
        float len = length();
        if (len > 0) {
            return Vector3(x/len, y/len, z/len);
        }
        return *this;
    }
};

struct Matrix4x4 {
    float m[4][4];
    
    Matrix4x4() {
        identity();
    }
    
    void identity() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] = (i == j) ? 1.0f : 0.0f;
            }
        }
    }
    
    static Matrix4x4 rotationX(float angle) {
        Matrix4x4 mat;
        float c = std::cos(angle);
        float s = std::sin(angle);
        
        mat.m[1][1] = c;
        mat.m[1][2] = -s;
        mat.m[2][1] = s;
        mat.m[2][2] = c;
        
        return mat;
    }
};

class GeometryTransformer {
public:
    static std::vector<Vector3> generateSphere(int segments = 16) {
        std::vector<Vector3> vertices;
        
        for (int i = 0; i <= segments; i++) {
            float lat = M_PI * i / segments;
            float sinLat = std::sin(lat);
            float cosLat = std::cos(lat);
            
            for (int j = 0; j <= segments; j++) {
                float lon = 2 * M_PI * j / segments;
                float sinLon = std::sin(lon);
                float cosLon = std::cos(lon);
                
                vertices.emplace_back(
                    sinLat * cosLon,
                    cosLat,
                    sinLat * sinLon
                );
            }
        }
        
        return vertices;
    }
};