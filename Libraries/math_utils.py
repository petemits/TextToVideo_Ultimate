import numpy as np
import math

class MathUtils:
    EPSILON = 1e-10
    PI = math.pi
    
    @staticmethod
    def lerp(a, b, t):
        return a + (b - a) * t
    
    @staticmethod
    def clamp(x, min_val, max_val):
        return min(max(x, min_val), max_val)
    
    @staticmethod
    def smoothstep(edge0, edge1, x):
        x = np.clip((x - edge0) / (edge1 - edge0), 0.0, 1.0)
        return x * x * (3.0 - 2.0 * x)
    
    @staticmethod
    def gaussian(x, mean, std):
        exponent = -0.5 * ((x - mean) / std) ** 2
        return math.exp(exponent) / (std * math.sqrt(2 * math.pi))

if __name__ == "__main__":
    print("Math Utilities Library")