import numpy as np
import math

class MatrixTransforms:
    @staticmethod
    def identity_matrix(dim=4):
        return np.eye(dim, dtype=np.float32)
    
    @staticmethod
    def translation_matrix(tx, ty, tz=0.0):
        mat = np.eye(4, dtype=np.float32)
        mat[0, 3] = tx
        mat[1, 3] = ty
        mat[2, 3] = tz
        return mat
    
    @staticmethod
    def rotation_matrix_x(angle):
        cos_a = math.cos(angle)
        sin_a = math.sin(angle)
        
        mat = np.eye(4, dtype=np.float32)
        mat[1, 1] = cos_a
        mat[1, 2] = -sin_a
        mat[2, 1] = sin_a
        mat[2, 2] = cos_a
        return mat

if __name__ == "__main__":
    print("Matrix Transforms Library")