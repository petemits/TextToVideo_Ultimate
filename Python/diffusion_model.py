import torch
import torch.nn as nn
import torch.nn.functional as F
import numpy as np
from typing import Optional, Tuple

class DiffusionConfig:
    """Configuration for diffusion model"""
    
    def __init__(self):
        self.timesteps = 1000
        self.beta_start = 0.0001
        self.beta_end = 0.02
        self.device = "cuda" if torch.cuda.is_available() else "cpu"

class NoiseScheduler:
    """Noise scheduler for diffusion models"""
    
    def __init__(self, config: DiffusionConfig):
        self.config = config
        self.timesteps = config.timesteps
        
        self.betas = torch.linspace(
            config.beta_start, config.beta_end, config.timesteps, 
            dtype=torch.float32, device=config.device
        )
        
        self.alphas = 1.0 - self.betas
        self.alphas_cumprod = torch.cumprod(self.alphas, dim=0)
    
    def add_noise(self, x_start: torch.Tensor, t: torch.Tensor, 
                 noise: Optional[torch.Tensor] = None) -> Tuple[torch.Tensor, torch.Tensor]:
        """Add noise to x_start at timestep t"""
        if noise is None:
            noise = torch.randn_like(x_start)
        
        sqrt_alphas_cumprod_t = torch.sqrt(self.alphas_cumprod[t]).view(-1, 1, 1, 1)
        sqrt_one_minus_alphas_cumprod_t = torch.sqrt(1.0 - self.alphas_cumprod[t]).view(-1, 1, 1, 1)
        
        noisy = sqrt_alphas_cumprod_t * x_start + sqrt_one_minus_alphas_cumprod_t * noise
        return noisy, noise

class UNetBlock(nn.Module):
    """Basic UNet block"""
    
    def __init__(self, in_channels: int, out_channels: int):
        super().__init__()
        
        self.conv1 = nn.Conv2d(in_channels, out_channels, 3, padding=1)
        self.conv2 = nn.Conv2d(out_channels, out_channels, 3, padding=1)
        self.relu = nn.ReLU()
        
    def forward(self, x: torch.Tensor) -> torch.Tensor:
        x = self.conv1(x)
        x = self.relu(x)
        x = self.conv2(x)
        x = self.relu(x)
        return x

class DiffusionModel(nn.Module):
    """Simple diffusion model"""
    
    def __init__(self, config: DiffusionConfig = None):
        super().__init__()
        self.config = config or DiffusionConfig()
        
        self.unet = UNetBlock(3, 64)
        self.scheduler = NoiseScheduler(config)
        
        self.to(self.config.device)
    
    def forward(self, x: torch.Tensor, t: torch.Tensor) -> torch.Tensor:
        """Forward pass"""
        return self.unet(x)

if __name__ == "__main__":
    config = DiffusionConfig()
    model = DiffusionModel(config)
    print(f"Diffusion Model initialized on {config.device}")