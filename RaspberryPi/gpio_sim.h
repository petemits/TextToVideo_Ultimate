#ifndef GPIO_SIM_H
#define GPIO_SIM_H

#include <stdint.h>
#include <stdbool.h>

// GPIO Pin modes
typedef enum {
    GPIO_INPUT = 0,
    GPIO_OUTPUT = 1
} GPIOMode;

// GPIO structure
typedef struct {
    void* gpio_map;
    uint32_t gpio_base;
} GPIOHandle;

// Function prototypes
bool gpio_init(GPIOHandle* handle);
void gpio_set_mode(uint8_t pin, GPIOMode mode);
void gpio_write(uint8_t pin, bool value);
bool gpio_read(uint8_t pin);
void delay_us(uint32_t microseconds);

#endif // GPIO_SIM_H