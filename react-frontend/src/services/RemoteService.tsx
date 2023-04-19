import axios, {AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse} from 'axios';
import 'react-toastify/dist/ReactToastify.css';
import {presentErrorToast} from "../common/ToastComponent";

class RemoteService {
    private instance: AxiosInstance;

    constructor() {
        this.instance = axios.create({
            baseURL: 'http://localhost:8080',
            timeout: 5000,
            headers: {
                'Content-Type': 'application/json',
            },
        });
    }

    private handleResponse<T>(response: AxiosResponse<T>): T {

        if (response.status === 200) {

            return response.data;

        } else {

            presentErrorToast(`Http error occurred with status ${response.status}!`);
            throw new AxiosError(`Http error occurred with status ${response.status}!`)
        }
    }

    get<T>(path: string, config?: AxiosRequestConfig): Promise<T> {
        return this.instance
            .get<T>(path, config)
            .then((response) => this.handleResponse<T>(response));
    }


    post<T>(path: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
        return this.instance
            .post<T>(path, data, config)
            .then((response) => this.handleResponse<T>(response));
    }


    put<T>(path: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
        return this.instance
            .put<T>(path, data, config)
            .then((response) => this.handleResponse<T>(response));
    }


    delete<T>(path: string, config?: AxiosRequestConfig): Promise<T> {
        return this.instance
            .delete<T>(path, config)
            .then((response) => this.handleResponse<T>(response));
    }
}

const remoteService = new RemoteService();

export default remoteService;