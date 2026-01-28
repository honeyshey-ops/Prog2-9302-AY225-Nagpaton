// Method 1: Using Web Audio API (recommended)
function playBeep() {
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const oscillator = audioContext.createOscillator();
    const gainNode = audioContext.createGain();
    
    oscillator.connect(gainNode);
    gainNode.connect(audioContext.destination);
    
    // Set frequency (440 Hz = A note)
    oscillator.frequency.value = 800;
    
    // Set volume
    gainNode.gain.value = 0.3;
    
    // Play for 200ms
    oscillator.start(audioContext.currentTime);
    oscillator.stop(audioContext.currentTime + 0.2);
}

// Method 2: Alternative using an audio element with data URI
function playBeepAlt() {
    const audio = new Audio('data:audio/wav;base64,UklGRnoGAABXQVZFZm10IBAAAAABAAEAQB8AAEAfAAABAAgAZGF0YQoGAACBhYqFbF1fdJivrJBhNjVgodDbq2EcBj+a2/LDciUFLIHO8tiJNwgZaLvt559NEAxQp+PwtmMcBjiR1/LMeSwFJHfH8N2QQAoUXrTp66hVFApGn+DyvmwhBSuBzvLZiTUHGGS96+OlUhELTqXh8bllHgU2jdXux3UsB2Z5x/DdkEAKFF607OujVBIJP5fW7r1sFQcpf8rwz4A2Bxpmu+vjpFIRC0yo4fK2YhwHNo/W7tCDOgcme8fw3Y9BCRVbtentpFQSCT+X1u6+bBYHKX/K8M+ANgcZZrrs46NSEQ1LqOHxtmIdBzWP1e7Qgzt4');
    audio.play();
}

// Method 3: Simple beep using oscillator with different frequencies
function playBeepCustom(frequency = 800, duration = 200, volume = 0.3) {
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const oscillator = audioContext.createOscillator();
    const gainNode = audioContext.createGain();
    
    oscillator.connect(gainNode);
    gainNode.connect(audioContext.destination);
    
    oscillator.frequency.value = frequency;
    gainNode.gain.value = volume;
    
    oscillator.start(audioContext.currentTime);
    oscillator.stop(audioContext.currentTime + duration / 1000);
}