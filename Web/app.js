class TextToVideoApp {
    constructor() {
        this.currentTaskId = null;
        
        // DOM Elements
        this.elements = {
            promptInput: document.getElementById('prompt-input'),
            generateBtn: document.getElementById('generate-btn'),
            clearBtn: document.getElementById('clear-btn'),
            downloadBtn: document.getElementById('download-btn'),
            durationSlider: document.getElementById('duration'),
            durationValue: document.getElementById('duration-value'),
            styleSelect: document.getElementById('style'),
            previewPlaceholder: document.getElementById('preview-placeholder'),
            videoPreview: document.getElementById('video-preview'),
            tasksList: document.getElementById('tasks-list')
        };
        
        // Initialize
        this.init();
    }
    
    init() {
        // Event listeners
        this.elements.generateBtn.addEventListener('click', () => this.generateVideo());
        this.elements.clearBtn.addEventListener('click', () => this.clearInput());
        this.elements.downloadBtn.addEventListener('click', () => this.downloadVideo());
        this.elements.durationSlider.addEventListener('input', (e) => {
            this.elements.durationValue.textContent = \`\${e.target.value}s\`;
        });
    }
    
    async generateVideo() {
        const prompt = this.elements.promptInput.value.trim();
        if (!prompt) {
            alert('Please enter a video prompt');
            return;
        }
        
        // Disable generate button
        this.elements.generateBtn.disabled = true;
        this.elements.generateBtn.textContent = 'Generating...';
        
        // Get settings
        const config = {
            prompt: prompt,
            duration: parseInt(this.elements.durationSlider.value),
            style: this.elements.styleSelect.value
        };
        
        try {
            const response = await fetch('http://localhost:5000/api/generate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(config)
            });
            
            const result = await response.json();
            
            if (result.error) {
                throw new Error(result.error);
            }
            
            this.currentTaskId = result.task_id;
            alert(\`Task \${result.task_id} has been queued\`);
            
            // Add task to list
            this.addTaskToList(result.task_id, prompt);
            
        } catch (error) {
            alert(\`Error: \${error.message}\`);
            console.error('Generation error:', error);
        } finally {
            // Re-enable generate button
            this.elements.generateBtn.disabled = false;
            this.elements.generateBtn.textContent = 'Generate Video';
        }
    }
    
    addTaskToList(taskId, prompt) {
        const taskElement = document.createElement('div');
        taskElement.className = 'task-item';
        taskElement.innerHTML = \`
            <div><strong>ID:</strong> \${taskId}</div>
            <div><strong>Prompt:</strong> \${prompt.substring(0, 50)}\${prompt.length > 50 ? '...' : ''}</div>
            <div><strong>Status:</strong> Queued</div>
        \`;
        
        this.elements.tasksList.appendChild(taskElement);
    }
    
    clearInput() {
        this.elements.promptInput.value = '';
        this.elements.promptInput.focus();
    }
    
    async downloadVideo() {
        if (!this.currentTaskId) return;
        
        alert('Download functionality will be implemented');
    }
}

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new TextToVideoApp();
});