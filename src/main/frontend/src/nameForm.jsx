import React, { useState, useEffect } from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';  // Adjust path if needed


function NameForm() {
    const [name, setName] = useState('');
    const [error, setError] = useState('');
    const [response, setResponse] = useState('');
    const [typedResponse, setTypedResponse] = useState('');
    const [typingIndex, setTypingIndex] = useState(0);

    useEffect(() => {
        if (response && typingIndex < response.length) {
            const timeout = setTimeout(() => {
                setTypedResponse(response.slice(0, typingIndex + 1)); // Add next character
                setTypingIndex(typingIndex + 1);
            }, 10); // Adjust typing speed here (30ms is more stable)
            return () => clearTimeout(timeout); // Cleanup on re-renders
        }
    }, [response, typingIndex]); // Rerun effect when response or index changes

    const handleSubmit = (event) => {
        event.preventDefault();
        setResponse('');
        setError('');
        setTypedResponse('');
        setTypingIndex(0); // Reset typing effect

        if (name.length < 3) {
            setError('Name must be at least 3 characters long.');
            return;
        }

        fetch('/submit', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ name: name })
        })
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            return response.text();
        })
        .then(data => {
            setResponse(data);
            setTypedResponse(''); // Reset typing effect before displaying
            setTypingIndex(0);
        })
        .catch(error => {
            console.error('Error:', error);
            setError('Error submitting form. Please try again.');
        });
    };

    return (
        <div className="card shadow-sm">
            <div className="card-header bg-primary text-white">
                <h1 className="text-center">Chat with ChatGPT - Steven</h1>
            </div>
            <div className="card-body">
                {error && <div className="alert alert-danger text-center">{error}</div>}

                {/* Typing effect for response */}
                {typedResponse && (
                    <div className="p-3 bg-light border rounded" style={{ minHeight: '50px', whiteSpace: 'pre-wrap' }}>
                        {typedResponse}
                        <span className="typing-cursor">|</span> {/* Cursor effect */}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="mt-4">
                    <div className="mb-3">
                        <label htmlFor="name" className="form-label">Enter your messages:</label>
                        <input
                            type="text"
                            id="name"
                            className="form-control"
                            placeholder="Type your message here"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary w-100">Submit</button>
                </form>
            </div>
        </div>
    );
}

// Render the React component
const rootElement = document.getElementById('react-root');
const root = createRoot(rootElement);
root.render(<NameForm />);
