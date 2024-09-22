import {Alert} from 'react-bootstrap';

export const AlertBar = (messages) => {

    return Array.isArray(messages.info) ? (
        <Alert variant={messages.type ? messages.type : "success"}>
            <ul>
                {messages.info.map((msg, index) => (
                    <li key={index}>{msg}</li>
                ))}
            </ul>
        </Alert>
    ) : (
        messages.info && <Alert variant={messages.type ? messages.type : "success"}>{messages.info}</Alert>
    )
}

