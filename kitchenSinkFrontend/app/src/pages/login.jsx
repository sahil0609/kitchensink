import React, { useEffect, useState } from 'react';
import { useAuth } from '../Context/LoginContext';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import {Config } from '../Data/Config';

const Signup = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });

    const [errorData, setErrorData] = useState(null);

    const {user, login, logout} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if(user !== null) {
            navigate('/members');
        }
    }, [user])

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        fetch(Config.LOGIN, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        }).then(response => {
            if(response.status === 401) {
                 setErrorData('Invalid email or password');
            }
            console.log(response.headers);
            const token = response.headers.get('authorization')
            console.log(token);
            if(token !== null) {
                setErrorData(null);
                login(token);
            }
    })
        .catch((error) => {
            setErrorData('Some errror occured');
            console.error('Error:', error);
        });
        
    };

    return (
        <Container>  
        {errorData && (
            <div className="alert alert-danger" role="alert">
                {errorData}
            </div>
             )}
            <Row className="justify-content-md-center">
                <Col md={6}>
                    <h2 className="text-center">Sign in</h2>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formEmailLogin">
                            <Form.Label>Email address</Form.Label>
                            <Form.Control
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                placeholder="Enter email"
                                required
                            />
                        </Form.Group>

                        <Form.Group controlId="formPasswordLogin">
                            <Form.Label>Password</Form.Label>
                            <Form.Control
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                placeholder="Password"
                                required
                            />
                        </Form.Group>

                        <Button variant="primary" type="submit" className="mt-3">
                            Sign in
                        </Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
};

export default Signup;