import React, { useState, useEffect } from 'react';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import {Config } from '../Data/Config';
import { useAuth } from '../Context/LoginContext';


const Signup = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        verifyPassword: '',
    });

    const [errorData, setErrorData] = useState(null);


    const {user} = useAuth();
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

    const handleSubmit =  async (e) => {
        e.preventDefault();

        if(formData.password !== formData.verifyPassword) {
            setErrorData('Passwords do not match');
            return;
        }

        const response = await fetch(Config.SIGNUP, {

            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        })

        if(response.status === 201) {
            setErrorData(null);
            navigate('/');
            
        }
        if(response.status === 400) {
            const responseData = await response.json();
            setErrorData(responseData.message);
            return
        }
        setErrorData(['An error occurred']);
        

    };

    return (
        <Container>
            {errorData && (
            <div className="alert alert-danger" role="alert">
                <ul>
                    {errorData.map((error, index) => (
                        <li key={index}>{error}</li>
                    ))}
                </ul>
            </div>
             )}
            <Row className="justify-content-md-center">
                <Col md={6}>
                    <h2 className="text-center">Sign Up</h2>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group controlId="formEmail">
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

                        <Form.Group controlId="formPassword">
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

                        <Form.Group controlId="formPassword">
                            <Form.Label>re enter password</Form.Label>
                            <Form.Control
                                type="password"
                                name="verifyPassword"
                                value={formData.verifyPassword}
                                onChange={handleChange}
                                placeholder="Password"
                                required
                            />
                        </Form.Group>

                        <Button variant="primary" type="submit" className="mt-3">
                            Sign Up
                        </Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    );
};

export default Signup;