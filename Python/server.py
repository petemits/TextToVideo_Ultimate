from flask import Flask, request, jsonify, send_file
from flask_cors import CORS
import os
import json
import time
from pathlib import Path

app = Flask(__name__)
CORS(app)

# Task storage
tasks = {}
task_counter = 1

@app.route('/')
def index():
    return jsonify({"status": "running", "service": "Text-to-Video AI Server"})

@app.route('/api/status', methods=['GET'])
def get_status():
    return jsonify({
        "status": "online",
        "timestamp": time.time(),
        "tasks": len(tasks)
    })

@app.route('/api/generate', methods=['POST'])
def generate_video():
    global task_counter
    
    try:
        data = request.get_json()
        
        if not data or 'prompt' not in data:
            return jsonify({"error": "Missing 'prompt' in request"}), 400
        
        prompt = data['prompt']
        task_id = f"task_{task_counter}"
        task_counter += 1
        
        tasks[task_id] = {
            "id": task_id,
            "prompt": prompt,
            "status": "queued",
            "progress": 0,
            "created_at": time.time()
        }
        
        return jsonify({
            "task_id": task_id,
            "status": "queued",
            "message": "Task created successfully"
        })
        
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/api/task/<task_id>', methods=['GET'])
def get_task_status(task_id: str):
    task = tasks.get(task_id)
    
    if not task:
        return jsonify({"error": "Task not found"}), 404
    
    return jsonify({
        "task_id": task_id,
        "prompt": task["prompt"],
        "status": task["status"],
        "progress": task["progress"]
    })

@app.route('/api/tasks', methods=['GET'])
def list_tasks():
    task_list = [{
        "task_id": task["id"],
        "prompt": task["prompt"][:50] + "..." if len(task["prompt"]) > 50 else task["prompt"],
        "status": task["status"],
        "progress": task["progress"]
    } for task in tasks.values()]
    
    return jsonify({
        "tasks": task_list,
        "total": len(task_list)
    })

@app.route('/api/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy", "timestamp": time.time()})

if __name__ == '__main__':
    print("Starting Text-to-Video AI Server...")
    print("API available at http://localhost:5000/api")
    app.run(host='0.0.0.0', port=5000, debug=False, threaded=True)