
export interface Response <T>{
    resource: T;
    messageStatus: string;
    status: number;
}

export interface ResponseError{
    messageError: string;
    messageStatus: string;
    status: number;
}

export interface ResponseImage{
    result: number;
}

export interface ResponseFeedfoward{
    uuid: string;
    outputs: number[];
}

export interface ResponseTrain{
    uuid: string;
    outputs: number[];
    biases: number[][];
    weights: number[][][];
}

export interface ResponseUUIDs{
    uuids: string[];
}

export interface ResponseNeuralNetwork{
    neuralNetwork: NeuralNetwork;
}

export interface ResponseUUID{
    uuid: string;
}

interface NeuralNetwork{
    uuid: string;
    outputsNodes: number;
    layers: number;
    learningRate: number;
    weights: number[][][];
    biases: number[][];
    outputs: number[];
}