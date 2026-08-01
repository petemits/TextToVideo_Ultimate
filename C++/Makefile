CC = g++
CFLAGS = -O3 -march=native -mtune=native -ffast-math -fopenmp
LDFLAGS = -lopencv_core -lopencv_imgproc -lopencv_videoio -lstdc++fs -lpthread -fopenmp
TARGETS = video_encoder frame_processor memory_manager vector_ops

all: $(TARGETS)

video_encoder: video_encoder.cpp
	$(CC) $(CFLAGS) -o ../bin/video_encoder video_encoder.cpp $(LDFLAGS)

frame_processor: frame_processor.cpp
	$(CC) $(CFLAGS) -o ../bin/frame_processor frame_processor.cpp $(LDFLAGS)

memory_manager: memory_manager.cpp
	$(CC) $(CFLAGS) -o ../bin/memory_manager memory_manager.cpp $(LDFLAGS)

vector_ops: vector_ops.cpp
	$(CC) $(CFLAGS) -o ../bin/vector_ops vector_ops.cpp $(LDFLAGS)

clean:
	rm -f ../bin/*.exe ../bin/*.o

install:
	mkdir -p ../bin