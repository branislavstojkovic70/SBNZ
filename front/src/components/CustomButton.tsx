import React from 'react';
import styled from 'styled-components';

interface CustomButtonProps {
  text: string;
  onClick: () => void;
}

const Button = styled.button`
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #0056b3;
  }
`;

const CustomButton: React.FC<CustomButtonProps> = ({ text, onClick }) => {
  return (
    <Button onClick={onClick}>
      {text}
    </Button>
  );
};

export default CustomButton;
